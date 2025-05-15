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

        paymentList.add(new Payment(1, "John Doe", "3500.00", 2));
        paymentList.add(new Payment(2, "Alice Smith", "3500.00", 2));
        paymentList.add(new Payment(3, "Bob Johnson", "1750.00", 1));
        paymentList.add(new Payment(4, "Lakshan Maduranga", "5250.00", 3));
        paymentList.add(new Payment(5, "Vihanga Heshan", "5250.00", 3));

        laborCount.setText(paymentList.size()+" "+"Labors");
        paymentAdapter = new PaymentAdapter(paymentList);
        payoutsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        payoutsRecyclerView.setAdapter(paymentAdapter);
    }
}