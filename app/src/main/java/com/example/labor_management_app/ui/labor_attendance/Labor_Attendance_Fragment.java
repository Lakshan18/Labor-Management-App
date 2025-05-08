package com.example.labor_management_app.ui.labor_attendance;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.labor_management_app.R;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_labor_attendance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize views
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

        // Set initial active filter
        setActiveFilter(weeklyFilter);

        // Setup click listeners for filters
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

        // Load initial data
        loadAllLabors();
        loadWeeklyData();
        setupLaborDropdown();

        // Setup attendance buttons
        btnPresent.setOnClickListener(v -> markAttendance(true));
        btnAbsent.setOnClickListener(v -> markAttendance(false));
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
        // This would come from your data source in a real app
        allLabors.add(new Labor_Attendance_View_Model("John Smith", 4.8f, 98, "https://example.com/image1.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Emma Johnson", 4.7f, 97, "https://example.com/image2.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Michael Brown", 4.6f, 96, "https://example.com/image3.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Sarah Davis", 4.5f, 95, "https://example.com/image4.jpg"));
        allLabors.add(new Labor_Attendance_View_Model("Robert Wilson", 4.4f, 94, "https://example.com/image5.jpg"));
    }

    private void setupLaborDropdown() {
        // Create list of labor names
        List<String> laborNames = new ArrayList<>();
        for (Labor_Attendance_View_Model labor : allLabors) {
            laborNames.add(labor.getName());
        }

        // Create adapter for dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                requireContext(),
                R.layout.dropdown_menu_item,
                laborNames
        );

        laborDropdown.setAdapter(adapter);
        laborDropdown.setOnItemClickListener((parent, view, position, id) -> {
            Labor_Attendance_View_Model selected = allLabors.get(position);
            showSelectedLabor(selected);
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
        String laborName = selectedLaborName.getText().toString();
        String status = isPresent ? "Present" : "Absent";

        // Here you would save to your database
        Toast.makeText(requireContext(),
                "Marked " + laborName + " as " + status,
                Toast.LENGTH_SHORT).show();

        // Reset selection
        laborDropdown.setText("");
        selectedLaborCard.setVisibility(View.GONE);
    }

    // Sample data methods
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