package com.example.labor_management_app.ui.view_labors;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.labor_management_app.CustomToast;
import com.example.labor_management_app.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.HashMap;
import java.util.Map;

public class View_Labors_Fragment extends Fragment {

    private TextInputEditText etEmployeeId;
    private ScrollView detailsScrollView;
    private AutoCompleteTextView autoCompleteWorkingArea;
    private Map<String, Map<String, String>> laborDatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_labors, container, false);

        etEmployeeId = view.findViewById(R.id.etEmployeeId);
        detailsScrollView = view.findViewById(R.id.laborDetailsForm);
        autoCompleteWorkingArea = view.findViewById(R.id.autoCompleteWorkingArea);

        TextView tvFullName = view.findViewById(R.id.textView6);
        TextView tvAge = view.findViewById(R.id.textView8);
        TextView tvContact = view.findViewById(R.id.etContactNumber);
        TextView tvNIC = view.findViewById(R.id.textView17);
        TextView tvRegDate = view.findViewById(R.id.textView19);
        TextView tvAddress1 = view.findViewById(R.id.etAddressLine1);
        TextView tvAddress2 = view.findViewById(R.id.etAddressLine2);
        TextView tvCity = view.findViewById(R.id.etCity);

        setupWorkingAreaDropdown();

        initializeLaborDatabase();

        detailsScrollView.setVisibility(View.GONE);

        etEmployeeId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String employeeId = s.toString().trim();

                if (employeeId.isEmpty()) {
                    detailsScrollView.setVisibility(View.GONE);
                    return;
                }

                if (laborDatabase.containsKey(employeeId)) {
                    detailsScrollView.setVisibility(View.VISIBLE);
                    Map<String, String> laborData = laborDatabase.get(employeeId);

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
                } else if (employeeId.length() >= 5) {
                    detailsScrollView.setVisibility(View.GONE);
                    CustomToast.showToast(requireContext(), "Invalid Labor ID", false);
                }
            }
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