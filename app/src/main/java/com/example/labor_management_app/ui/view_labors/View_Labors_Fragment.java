package com.example.labor_management_app.ui.view_labors;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.labor_management_app.CustomToast;
import com.example.labor_management_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class View_Labors_Fragment extends Fragment {

    private TextInputEditText etEmployeeId;
    private ScrollView detailsScrollView;
    private AutoCompleteTextView autoCompleteWorkingArea;
    private Map<String, Map<String, String>> laborDatabase;
    private MaterialButton updateDetailsBtn, laborPfBtn;
    private String currentEmployeeId = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_labors, container, false);

        // Initialize views
        etEmployeeId = view.findViewById(R.id.etEmployeeId);
        detailsScrollView = view.findViewById(R.id.laborDetailsForm);
        autoCompleteWorkingArea = view.findViewById(R.id.autoCompleteWorkingArea);
        updateDetailsBtn = view.findViewById(R.id.update_details_btn);
        laborPfBtn = view.findViewById(R.id.labor_pf_btn);

        TextView tvFullName = view.findViewById(R.id.textView6);
        TextView tvAge = view.findViewById(R.id.textView8);
        TextInputEditText tvContact = view.findViewById(R.id.etContactNumber);
        TextView tvNIC = view.findViewById(R.id.textView17);
        TextView tvRegDate = view.findViewById(R.id.textView19);
        TextInputEditText tvAddress1 = view.findViewById(R.id.etAddressLine1);
        TextInputEditText tvAddress2 = view.findViewById(R.id.etAddressLine2);
        TextInputEditText tvCity = view.findViewById(R.id.etCity);

        setupWorkingAreaDropdown();
        initializeLaborDatabase();
        detailsScrollView.setVisibility(View.GONE);

        // Load animation
        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click);

        // Employee ID text change listener
        etEmployeeId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                currentEmployeeId = s.toString().trim();

                if (currentEmployeeId.isEmpty()) {
                    detailsScrollView.setVisibility(View.GONE);
                    return;
                }

                if (laborDatabase.containsKey(currentEmployeeId)) {
                    detailsScrollView.setVisibility(View.VISIBLE);
                    Map<String, String> laborData = laborDatabase.get(currentEmployeeId);

                    tvFullName.setText(laborData.get("fullName"));
                    tvAge.setText(laborData.get("age"));
                    tvContact.setText(laborData.get("contact"));
                    tvNIC.setText(laborData.get("nic"));
                    tvRegDate.setText(laborData.get("regDate"));
                    tvAddress1.setText(laborData.get("address1"));
                    tvAddress2.setText(laborData.get("address2"));
                    tvCity.setText(laborData.get("city"));
                    autoCompleteWorkingArea.setText(laborData.get("workingArea"));

                    CustomToast.showToast(requireContext(), "Labor details found", true);
                } else if (currentEmployeeId.length() >= 5) {
                    detailsScrollView.setVisibility(View.GONE);
                    CustomToast.showToast(requireContext(), "Invalid Labor ID", false);
                }
            }
        });

        // Update Details Button Click Listener
        updateDetailsBtn.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);

            if (currentEmployeeId.isEmpty() || !laborDatabase.containsKey(currentEmployeeId)) {
                CustomToast.showToast(requireContext(), "Please enter a valid Labor ID first", false);
                return;
            }

            // Get updated values
            String updatedContact = tvContact.getText().toString().trim();
            String updatedAddress1 = tvAddress1.getText().toString().trim();
            String updatedAddress2 = tvAddress2.getText().toString().trim();
            String updatedCity = tvCity.getText().toString().trim();
            String updatedWorkingArea = autoCompleteWorkingArea.getText().toString().trim();

            // Validate inputs
            if (updatedContact.isEmpty() || updatedAddress1.isEmpty() || updatedCity.isEmpty()) {
                CustomToast.showToast(requireContext(), "Please fill all required fields", false);
                return;
            }

            // Update labor data
            Map<String, String> laborData = laborDatabase.get(currentEmployeeId);
            laborData.put("contact", updatedContact);
            laborData.put("address1", updatedAddress1);
            laborData.put("address2", updatedAddress2);
            laborData.put("city", updatedCity);
            laborData.put("workingArea", updatedWorkingArea);

            CustomToast.showToast(requireContext(), "Labor details updated successfully", true);
        });

        // Labor PF Button Click Listener
        laborPfBtn.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);

            if (currentEmployeeId.isEmpty() || !laborDatabase.containsKey(currentEmployeeId)) {
                CustomToast.showToast(requireContext(), "Please enter a valid Labor ID first", false);
                return;
            }

            // Here you would typically navigate to a PF details fragment
            // For now, we'll just show a toast
            String laborName = laborDatabase.get(currentEmployeeId).get("fullName");
            CustomToast.showToast(requireContext(),
                    "Opening PF details for " + laborName, true);

            // Example of navigation:
            // Navigation.findNavController(v).navigate(R.id.action_to_pfFragment);
        });

        return view;
    }

    private void setupWorkingAreaDropdown() {
        String[] workingAreas = getResources().getStringArray(R.array.working_areas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                workingAreas
        );

        autoCompleteWorkingArea.setAdapter(adapter);
    }

    private void initializeLaborDatabase() {
        laborDatabase = new HashMap<>();

        Map<String, String> labor1 = new HashMap<>();
        labor1.put("fullName", "Sahan Gamage");
        labor1.put("age", "18");
        labor1.put("contact", "0712345678");
        labor1.put("nic", "200763381612");
        labor1.put("regDate", "4/5/2025");
        labor1.put("address1", "123 Main Street");
        labor1.put("address2", "Colombo 05");
        labor1.put("city", "Colombo");
        labor1.put("workingArea", "Kitchen");
        laborDatabase.put("60100", labor1);

        Map<String, String> labor2 = new HashMap<>();
        labor2.put("fullName", "John Doe");
        labor2.put("age", "25");
        labor2.put("contact", "0776543210");
        labor2.put("nic", "199512345678");
        labor2.put("regDate", "15/3/2024");
        labor2.put("address1", "456 Oak Avenue");
        labor2.put("address2", "Kandy");
        labor2.put("city", "Kandy");
        labor2.put("workingArea", "Production");
        laborDatabase.put("60101", labor2);
    }
}