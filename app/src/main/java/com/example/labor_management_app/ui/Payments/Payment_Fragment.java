package com.example.labor_management_app.ui.Payments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.labor_management_app.R;

import java.util.ArrayList;
import java.util.List;


public class Payment_Fragment extends Fragment {

    private RecyclerView payoutsRecyclerView;
    private PaymentAdapter paymentAdapter;
    private List<Payment> paymentList = new ArrayList<>();

    private TextView laborCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment, container, false);
        payoutsRecyclerView = view.findViewById(R.id.payoutsRecyclerView);

        laborCount = view.findViewById(R.id.remainingCount);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        List<Payment.WorkDay> aliceWorkDays = new ArrayList<>();
        aliceWorkDays.add(new Payment.WorkDay("Monday", "8:00 AM - 5:00 PM"));
        aliceWorkDays.add(new Payment.WorkDay("Wednesday", "7:00 AM - 5:00 PM"));
        aliceWorkDays.add(new Payment.WorkDay("Thursday", "8:00 AM - 5:00 PM"));

        List<Payment.WorkDay> johnFoeWorkDays = new ArrayList<>();
        johnFoeWorkDays.add(new Payment.WorkDay("Monday", "8:00 AM - 5:00 PM"));
        johnFoeWorkDays.add(new Payment.WorkDay("Wednesday", "Absent"));
        johnFoeWorkDays.add(new Payment.WorkDay("Thursday", "7:00 AM - 5:00 PM"));

        paymentList.add(new Payment(1, "Alice Smith", aliceWorkDays));
        paymentList.add(new Payment(2, "John Foster", johnFoeWorkDays));

        laborCount.setText(paymentList.size() + " Labors");
        paymentAdapter = new PaymentAdapter(paymentList, requireContext());
        payoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        payoutsRecyclerView.setAdapter(paymentAdapter);
    }
}