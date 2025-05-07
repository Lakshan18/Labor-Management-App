package com.example.labor_management_app.ui.view_labors;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.labor_management_app.CustomToast;
import com.example.labor_management_app.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

public class View_Labors_Fragment extends Fragment {

    private TextInputEditText etEmployeeId;
    private ScrollView detailsScrollView;
    private AutoCompleteTextView autoCompleteWorkingArea;
    private Map<String, Map<String, String>> laborDatabase;
    private Map<String, List<Integer>> laborAttendance;
    private MaterialButton updateDetailsBtn, laborPfBtn;
    private String currentEmployeeId = "";
    private Calendar currentDisplayMonth;
    private TextView monthYearText;
    private LinearLayout calendarRowsContainer;
    private TextView attendanceText;
    private ProgressBar efficiencyProgress;
    private TextView presentDaysText;
    private TextView absentDaysText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_labors, container, false);

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
        initializeAttendanceData();
        detailsScrollView.setVisibility(View.GONE);

        final Animation buttonClickAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.button_click);

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

        updateDetailsBtn.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);
            if (currentEmployeeId.isEmpty() || !laborDatabase.containsKey(currentEmployeeId)) {
                CustomToast.showToast(requireContext(), "Please enter a valid Labor ID first", false);
                return;
            }
            String updatedContact = tvContact.getText().toString().trim();
            String updatedAddress1 = tvAddress1.getText().toString().trim();
            String updatedAddress2 = tvAddress2.getText().toString().trim();
            String updatedCity = tvCity.getText().toString().trim();
            String updatedWorkingArea = autoCompleteWorkingArea.getText().toString().trim();
            if (updatedContact.isEmpty() || updatedAddress1.isEmpty() || updatedCity.isEmpty()) {
                CustomToast.showToast(requireContext(), "Please fill all required fields", false);
                return;
            }
            Map<String, String> laborData = laborDatabase.get(currentEmployeeId);
            laborData.put("contact", updatedContact);
            laborData.put("address1", updatedAddress1);
            laborData.put("address2", updatedAddress2);
            laborData.put("city", updatedCity);
            laborData.put("workingArea", updatedWorkingArea);
            CustomToast.showToast(requireContext(), "Labor details updated successfully", true);
        });

        laborPfBtn.setOnClickListener(v -> {
            v.startAnimation(buttonClickAnimation);
            if (currentEmployeeId.isEmpty() || !laborDatabase.containsKey(currentEmployeeId)) {
                CustomToast.showToast(requireContext(), "Please enter a valid Labor ID first", false);
                return;
            }
            showPerformanceDialog();
        });

        return view;
    }

    private void initializeAttendanceData() {
        laborAttendance = new HashMap<>();
        List<Integer> sahanAttendance = new ArrayList<>();
        sahanAttendance.add(1); sahanAttendance.add(2); sahanAttendance.add(5); sahanAttendance.add(6);
        sahanAttendance.add(8); sahanAttendance.add(9); sahanAttendance.add(10); sahanAttendance.add(11); sahanAttendance.add(13);
        sahanAttendance.add(15); sahanAttendance.add(17); sahanAttendance.add(18); sahanAttendance.add(20);
        sahanAttendance.add(22); sahanAttendance.add(23); sahanAttendance.add(25); sahanAttendance.add(27);
        laborAttendance.put("60100", sahanAttendance);

        List<Integer> johnAttendance = new ArrayList<>();
        johnAttendance.add(1); johnAttendance.add(3); johnAttendance.add(5); johnAttendance.add(7);
        johnAttendance.add(8); johnAttendance.add(10); johnAttendance.add(12); johnAttendance.add(14);
        johnAttendance.add(16); johnAttendance.add(18); johnAttendance.add(20); johnAttendance.add(22);
        johnAttendance.add(24); johnAttendance.add(26); johnAttendance.add(28);
        laborAttendance.put("60101", johnAttendance);
    }

    private void showPerformanceDialog() {
        Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.labor_performance_dialog);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);

        RatingBar ratingBar = dialog.findViewById(R.id.performance_rating);
        efficiencyProgress = dialog.findViewById(R.id.efficiency_progress);
        attendanceText = dialog.findViewById(R.id.attendance_text);
        monthYearText = dialog.findViewById(R.id.calendar_month_year);
        calendarRowsContainer = dialog.findViewById(R.id.calendar_rows_container);
        Button btnCancel = dialog.findViewById(R.id.btn_cancel);
        Button btnSubmit = dialog.findViewById(R.id.btn_submit);
        ImageView btnPrev = dialog.findViewById(R.id.btn_prev_month);
        ImageView btnNext = dialog.findViewById(R.id.btn_next_month);
        presentDaysText = dialog.findViewById(R.id.present_days_text);
        absentDaysText = dialog.findViewById(R.id.absent_days_text);

        currentDisplayMonth = Calendar.getInstance();
        btnPrev.setOnClickListener(v -> navigateToPreviousMonth());
        btnNext.setOnClickListener(v -> navigateToNextMonth());
        updateCalendarView();

        String laborName = laborDatabase.get(currentEmployeeId).get("fullName");
        Random random = new Random();
        int efficiency = 65 + random.nextInt(30);
        efficiencyProgress.setProgress(efficiency);

        btnCancel.setOnClickListener(v -> {
            dialog.dismiss();
            CustomToast.showToast(requireContext(), "Performance review cancelled", false);
        });

        btnSubmit.setOnClickListener(v -> {
            float rating = ratingBar.getRating();
            dialog.dismiss();
            String message = String.format("%s's performance rated %.1f/5", laborName, rating);
            CustomToast.showToast(requireContext(), message, true);
        });

        dialog.show();
    }

    private void navigateToPreviousMonth() {
        currentDisplayMonth.add(Calendar.MONTH, -1);
        updateCalendarView();
    }

    private void navigateToNextMonth() {
        Calendar now = Calendar.getInstance();
        Calendar nextMonth = (Calendar) currentDisplayMonth.clone();
        nextMonth.add(Calendar.MONTH, 1);
        if (nextMonth.get(Calendar.YEAR) < now.get(Calendar.YEAR) ||
                (nextMonth.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                        nextMonth.get(Calendar.MONTH) <= now.get(Calendar.MONTH))) {
            currentDisplayMonth.add(Calendar.MONTH, 1);
            updateCalendarView();
        }
    }

    private void updateCalendarView() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        monthYearText.setText(monthFormat.format(currentDisplayMonth.getTime()));
        generateCalendar(calendarRowsContainer, currentDisplayMonth);
        updateAttendanceStats();
    }

    private void updateAttendanceStats() {
        int[] attendanceStats = calculateAttendanceStats(currentEmployeeId, currentDisplayMonth);
        int presentDays = attendanceStats[0];
        int absentDays = attendanceStats[1];
        int totalDays = attendanceStats[2];

        presentDaysText.setText(presentDays + " days");
        absentDaysText.setText(absentDays + " days");
        attendanceText.setText(calculateAttendancePercentage(presentDays, totalDays) + "%");
    }

    private int[] calculateAttendanceStats(String laborId, Calendar month) {
        int[] stats = new int[3];
        if (!laborAttendance.containsKey(laborId)) return stats;

        Calendar today = Calendar.getInstance();
        int daysInMonth = month.getActualMaximum(Calendar.DAY_OF_MONTH);
        int currentDay = today.get(Calendar.DAY_OF_MONTH);
        boolean isCurrentMonth = month.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                month.get(Calendar.MONTH) == today.get(Calendar.MONTH);

        int daysToCount = isCurrentMonth ? currentDay - 1 : daysInMonth;
        stats[2] = 0;

        List<Integer> attendedDays = laborAttendance.get(laborId);

        for (int day = 1; day <= daysToCount; day++) {
            month.set(Calendar.DAY_OF_MONTH, day);
            int dayOfWeek = month.get(Calendar.DAY_OF_WEEK);

            if (dayOfWeek != Calendar.SUNDAY) {
                stats[2]++;
                if (attendedDays.contains(day)) {
                    stats[0]++; // Present
                } else {
                    stats[1]++; // Absent
                }
            }
        }
        return stats;
    }

    private int calculateAttendancePercentage(int presentDays, int totalDays) {
        return totalDays == 0 ? 0 : (int) (((float) presentDays / totalDays) * 100);
    }

    private void generateCalendar(LinearLayout container, Calendar monthCalendar) {
        container.removeAllViews();
        Calendar today = Calendar.getInstance();
        boolean isCurrentMonth = monthCalendar.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                monthCalendar.get(Calendar.MONTH) == today.get(Calendar.MONTH);
        int todayDay = today.get(Calendar.DAY_OF_MONTH);
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = monthCalendar.get(Calendar.DAY_OF_WEEK);
        int daysInMonth = monthCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        firstDayOfWeek = firstDayOfWeek - 1;
        if (firstDayOfWeek == 0) firstDayOfWeek = 7;
        int currentDay = 1;
        int currentWeek = 0;

        while (currentDay <= daysInMonth) {
            LinearLayout weekRow = new LinearLayout(getContext());
            weekRow.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            weekRow.setOrientation(LinearLayout.HORIZONTAL);
            weekRow.setWeightSum(7);
            weekRow.setPadding(0, 4, 0, 4);

            for (int i = 1; i <= 7; i++) {
                TextView dayView = new TextView(getContext());
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        0, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
                params.setMargins(4, 0, 4, 0);
                dayView.setLayoutParams(params);
                dayView.setGravity(Gravity.CENTER);
                dayView.setTextSize(14);
                dayView.setPadding(0, 8, 0, 8);
                dayView.setBackgroundResource(R.drawable.calendar_day_bg);

                if ((currentWeek == 0 && i < firstDayOfWeek) || currentDay > daysInMonth) {
                    dayView.setText("");
                    dayView.setBackgroundColor(Color.TRANSPARENT);
                } else {
                    if (i == 7) { // Sunday
                        dayView.setBackgroundResource(R.drawable.holiday_rectangle);
                        dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                    } else {
                        dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.primaryDark));
                    }

                    dayView.setText(String.valueOf(currentDay));

                    if (isCurrentMonth && currentDay == todayDay) {
                        dayView.setBackgroundResource(R.drawable.circle_bg_current_day);
                        dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        dayView.setTypeface(null, Typeface.BOLD);
                    } else if (isCurrentMonth && currentDay > todayDay) {
                        dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.future_day));
                        dayView.setAlpha(0.6f);
                    } else if (i != 7) {
                        if (laborAttendance.containsKey(currentEmployeeId) &&
                                laborAttendance.get(currentEmployeeId).contains(currentDay)) {
                            dayView.setBackgroundResource(R.drawable.circle_bg_present);
                            dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        } else if (!isCurrentMonth || currentDay < todayDay) {
                            dayView.setBackgroundResource(R.drawable.circle_bg_absent);
                            dayView.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
                        }
                    }
                    currentDay++;
                }
                weekRow.addView(dayView);
            }
            container.addView(weekRow);
            currentWeek++;
        }
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