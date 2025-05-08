package com.example.labor_management_app.ui.labor_attendance;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.labor_management_app.CustomToast;
import com.example.labor_management_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Labor_Attendance_Fragment extends Fragment {

    private LinearLayout cardContainer;
    private TextView weeklyFilter, monthlyFilter, yearlyFilter;
    private AutoCompleteTextView laborDropdown;
    private TextInputLayout laborDropdownLayout;
    private LinearLayout selectedLaborCard;
    private ImageView selectedLaborImage;
    private TextView selectedLaborName, selectedLaborRatingText;
    private RatingBar selectedLaborRating;
    private Button btnPresent, btnAbsent;

    private List<Labor_Attendance_View_Model> allLabors = new ArrayList<>();
    private boolean isLaborSelected = false;

    private static final String DEFAULT_TIME = "Set Time";
    private String selectedInTime = DEFAULT_TIME;
    private String selectedOutTime = DEFAULT_TIME;

    public interface TimeSelectionListener {
        void onTimeSelected(String inTime, String outTime);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_labor_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeViews(view);
        setActiveFilter(weeklyFilter);
        setupFilterListeners();
        loadAllLabors();
        loadWeeklyData();
        setupLaborDropdown();
        setupAttendanceButtons();
    }

    private void initializeViews(View view) {
        cardContainer = view.findViewById(R.id.cardContainer);
        weeklyFilter = view.findViewById(R.id.weeklyFilter);
        monthlyFilter = view.findViewById(R.id.monthlyFilter);
        yearlyFilter = view.findViewById(R.id.yearlyFilter);
        laborDropdown = view.findViewById(R.id.laborDropdown);
        laborDropdownLayout = view.findViewById(R.id.laborDropdownLayout);
        selectedLaborCard = view.findViewById(R.id.selectedLaborCard);
        selectedLaborImage = view.findViewById(R.id.selectedLaborImage);
        selectedLaborName = view.findViewById(R.id.selectedLaborName);
        selectedLaborRating = view.findViewById(R.id.selectedLaborRating);
        selectedLaborRatingText = view.findViewById(R.id.selectedLaborRatingText);
        btnPresent = view.findViewById(R.id.btnPresent);
        btnAbsent = view.findViewById(R.id.btnAbsent);

        selectedLaborCard.setVisibility(View.GONE);
    }

    private void setupFilterListeners() {
        weeklyFilter.setOnClickListener(v -> {
            setActiveFilter(weeklyFilter);
            loadWeeklyData();
        });

        monthlyFilter.setOnClickListener(v -> {
            setActiveFilter(monthlyFilter);
            loadMonthlyData();
        });

        yearlyFilter.setOnClickListener(v -> {
            setActiveFilter(yearlyFilter);
            loadYearlyData();
        });
    }

    private void setupAttendanceButtons() {
        btnPresent.setOnClickListener(v -> {
            if (!isLaborSelected) {
                CustomToast.showToast(requireContext(), "Please select a labor before marking attendance", false);
                return;
            }
            markAttendance(true);
        });

        btnAbsent.setOnClickListener(v -> {
            if (!isLaborSelected) {
                CustomToast.showToast(requireContext(), "Please select a labor before marking attendance", false);
                return;
            }
            markAttendance(false);
        });
    }

    private void setActiveFilter(TextView activeFilter) {
        weeklyFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        monthlyFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));
        yearlyFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.grey));

        activeFilter.setTextColor(ContextCompat.getColor(requireContext(), R.color.green));
    }

    private void loadWeeklyData() {
        cardContainer.removeAllViews();
        List<Labor_Attendance_View_Model> weeklyTopLabors = getWeeklyTopLabors();
        for (Labor_Attendance_View_Model labor : weeklyTopLabors) {
            addLaborCard(labor);
        }
        setupResponsiveCards();
    }

    private void loadMonthlyData() {
        cardContainer.removeAllViews();
        List<Labor_Attendance_View_Model> monthlyTopLabors = getMonthlyTopLabors();
        for (Labor_Attendance_View_Model labor : monthlyTopLabors) {
            addLaborCard(labor);
        }
        setupResponsiveCards();
    }

    private void loadYearlyData() {
        cardContainer.removeAllViews();
        List<Labor_Attendance_View_Model> yearlyTopLabors = getYearlyTopLabors();
        for (Labor_Attendance_View_Model labor : yearlyTopLabors) {
            addLaborCard(labor);
        }
        setupResponsiveCards();
    }

    private void addLaborCard(Labor_Attendance_View_Model labor) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View cardView = inflater.inflate(R.layout.item_labor_card, cardContainer, false);

        TextView nameView = cardView.findViewById(R.id.laborName);
        RatingBar ratingBar = cardView.findViewById(R.id.laborRatingBar);
        TextView ratingText = cardView.findViewById(R.id.laborRatingText);
        TextView percentageView = cardView.findViewById(R.id.attendancePercentage);
        ImageView imageView = cardView.findViewById(R.id.laborImage);

        nameView.setText(labor.getName());
        ratingBar.setRating(labor.getRating());
        ratingText.setText(String.format("%.1f", labor.getRating()));
        percentageView.setText(labor.getAttendancePercentage() + "%");

        Glide.with(this)
                .load(labor.getImageUrl())
                .placeholder(R.drawable.ic_labor_placeholder)
                .circleCrop()
                .into(imageView);

        YoYo.with(Techniques.FadeIn)
                .duration(500)
                .playOn(cardView);

        cardContainer.addView(cardView);
    }

    private void setupResponsiveCards() {
        cardContainer.post(() -> {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int screenPadding = getResources().getDimensionPixelSize(R.dimen.screen_edge_padding);
            int cardMargin = getResources().getDimensionPixelSize(R.dimen.card_margin);
            int cardWidth = (screenWidth - (2 * screenPadding) - cardMargin) / 2;

            for (int i = 0; i < cardContainer.getChildCount(); i++) {
                View card = cardContainer.getChildAt(i);
                ViewGroup.LayoutParams params = card.getLayoutParams();
                params.width = cardWidth;
                card.setLayoutParams(params);
            }
        });
    }

    private void loadAllLabors() {
        allLabors.add(new Labor_Attendance_View_Model("John Smith", 4.8f, 98, "https://example.com/image1.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Emma Johnson", 4.7f, 97, "https://example.com/image2.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Michael Brown", 4.6f, 96, "https://example.com/image3.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Sarah Davis", 4.5f, 95, "https://example.com/image4.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Robert Wilson", 4.4f, 94, "https://example.com/image5.jpg"));
    }

    private void setupLaborDropdown() {
        List<String> laborNames = new ArrayList<>();
        laborNames.add("Select Labor"); // First item as default
        for (Labor_Attendance_View_Model labor : allLabors) {
            laborNames.add(labor.getName());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                laborNames
        );

        laborDropdown.setAdapter(adapter);
        laborDropdown.setText("Select Labor", false);

        laborDropdown.setOnItemClickListener((parent, view, position, id) -> {
            if (position == 0) {
                isLaborSelected = false;
                selectedLaborCard.setVisibility(View.GONE);
                CustomToast.showToast(requireContext(), "Please select a valid labor", false);
            } else {
                Labor_Attendance_View_Model selected = allLabors.get(position - 1);
                isLaborSelected = true;
                showSelectedLabor(selected);
            }
        });
    }

    private void showSelectedLabor(Labor_Attendance_View_Model labor) {
        selectedLaborCard.setVisibility(View.VISIBLE);
        selectedLaborName.setText(labor.getName());
        selectedLaborRating.setRating(labor.getRating());
        selectedLaborRatingText.setText(String.format("%.1f", labor.getRating()));

        Glide.with(this)
                .load(labor.getImageUrl())
                .placeholder(R.drawable.ic_labor_placeholder)
                .circleCrop()
                .into(selectedLaborImage);
    }

    private void markAttendance(boolean isPresent) {
        if (!isPresent) {
            // Handle absent case
            CustomToast.showToast(requireContext(), "Marked " + selectedLaborName.getText().toString() + " as Absent", true);
            resetSelection();
            return;
        }

        // Show time selection dialog for present
        showTimeSelectionDialog();
    }

    private void showTimeSelectionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_attendance_time, null);

        // Initialize dialog views
        Spinner spinnerInTime = dialogView.findViewById(R.id.spinnerInTime);
        Spinner spinnerOutTime = dialogView.findViewById(R.id.spinnerOutTime);
        Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        Button btnReset = dialogView.findViewById(R.id.btnReset);
        ImageView btnClose = dialogView.findViewById(R.id.btnClose);

        // Generate time list
        List<String> timeList = new ArrayList<>();
        timeList.add(DEFAULT_TIME);
        for (int hour = 6; hour <= 22; hour++) {
            for (int minute = 0; minute < 60; minute += 30) {
                String time = String.format("%02d:%02d", hour, minute);
                timeList.add(time);
            }
        }

        // Setup adapters
        ArrayAdapter<String> timeAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                timeList
        );
        timeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerInTime.setAdapter(timeAdapter);
        spinnerOutTime.setAdapter(timeAdapter);

        // Create dialog
        AlertDialog dialog = builder.setView(dialogView).create();
        dialog.setCanceledOnTouchOutside(false);

        // Set listeners
        btnClose.setOnClickListener(v -> dialog.dismiss());

        spinnerInTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedInTime = timeList.get(position);
                updateConfirmButtonState(btnConfirm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerOutTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOutTime = timeList.get(position);
                updateConfirmButtonState(btnConfirm);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        btnReset.setOnClickListener(v -> {
            spinnerInTime.setSelection(0);
            spinnerOutTime.setSelection(0);
        });

        btnConfirm.setOnClickListener(v -> {
            if (isValidTimeSelection()) {
                CustomToast.showToast(requireContext(),
                        "Attendance marked with In: " + selectedInTime + " Out: " + selectedOutTime,
                        true);
                resetSelection();
                dialog.dismiss();
            } else {
                CustomToast.showToast(requireContext(),
                        "Please select valid times",
                        false);
            }
        });

        dialog.show();
    }

    private void updateConfirmButtonState(Button btnConfirm) {
        boolean isValid = isValidTimeSelection();
        btnConfirm.setEnabled(isValid);
        btnConfirm.setAlpha(isValid ? 1f : 0.5f);
    }

    private boolean isValidTimeSelection() {
        return !selectedInTime.equals(DEFAULT_TIME) &&
                !selectedOutTime.equals(DEFAULT_TIME) &&
                isTimeOrderValid();
    }

    private boolean isTimeOrderValid() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
            Date inTime = sdf.parse(selectedInTime);
            Date outTime = sdf.parse(selectedOutTime);
            return outTime.after(inTime);
        } catch (ParseException e) {
            return false;
        }
    }

    private void resetSelection() {
        laborDropdown.setText("Select Labor", false);
        isLaborSelected = false;
        selectedLaborCard.setVisibility(View.GONE);
        selectedInTime = DEFAULT_TIME;
        selectedOutTime = DEFAULT_TIME;
    }
//    private void markAttendance(boolean isPresent) {
//        String laborName = selectedLaborName.getText().toString();
//        String status = isPresent ? "Present" : "Absent";
//
//        CustomToast.showToast(requireContext(), "Marked " + laborName + " as " + status, true);
//
//        laborDropdown.setText("Select Labor", false);
//        isLaborSelected = false;
//        selectedLaborCard.setVisibility(View.GONE);
//    }

    private List<Labor_Attendance_View_Model> getWeeklyTopLabors() {
        return allLabors.subList(0, Math.min(5, allLabors.size()));
    }

    private List<Labor_Attendance_View_Model> getMonthlyTopLabors() {
        return allLabors.subList(0, Math.min(5, allLabors.size()));
    }

    private List<Labor_Attendance_View_Model> getYearlyTopLabors() {
        return allLabors.subList(0, Math.min(5, allLabors.size()));
    }
}