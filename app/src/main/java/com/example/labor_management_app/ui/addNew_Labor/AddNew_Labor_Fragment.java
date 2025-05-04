package com.example.labor_management_app.ui.addNew_Labor;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.labor_management_app.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;

public class AddNew_Labor_Fragment extends Fragment {

    private TextInputEditText etRegisteredDate;
    private AutoCompleteTextView autoCompleteWorkingArea;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_new_labor, container, false);

        etRegisteredDate = view.findViewById(R.id.etRegisteredDate);
        autoCompleteWorkingArea = view.findViewById(R.id.autoCompleteWorkingArea);

        setupDatePicker();

        setupWorkingAreaDropdown();

        return view;
    }

    private void setupDatePicker() {
        etRegisteredDate.setOnClickListener(v -> {
            final Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(),
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        etRegisteredDate.setText(formattedDate);
                    },
                    year, month, day
            );

            datePickerDialog.show();
        });
    }

    private void setupWorkingAreaDropdown() {
        String[] workingAreas = getResources().getStringArray(R.array.working_areas);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                workingAreas
        );

        autoCompleteWorkingArea.setAdapter(adapter);

        autoCompleteWorkingArea.setOnItemClickListener((parent, view, position, id) -> {
            String selectedItem = (String) parent.getItemAtPosition(position);
            Toast.makeText(requireContext(), "Selected: " + selectedItem, Toast.LENGTH_SHORT).show();
        });
    }
}