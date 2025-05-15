package com.example.labor_management_app.ui.Payments;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Payment {
    private int lNo;
    private String name;
    private String amount;
    private int dayCount;
    private List<WorkDay> workDays;
    private boolean salaryConfirmed;

    public static class WorkDay {
        String date;
        String workingHours;
        double dailySalary;
        boolean absent;

        public WorkDay(String date, String workingHours) {
            this.date = date;
            this.workingHours = workingHours;
            this.absent = false;
            this.dailySalary = calculateDailySalary(workingHours);
        }

        public WorkDay(String date) { // For absent days
            this.date = date;
            this.absent = true;
            this.workingHours = "Absent";
            this.dailySalary = 0;
        }
    }

    public static double calculateDailySalary(String workingHours) {
        try {
            String[] times = workingHours.split(" - ");
            SimpleDateFormat format = new SimpleDateFormat("h:mm a", Locale.ENGLISH);

            Date start = format.parse(times[0]);
            Date end = format.parse(times[1]);

            long diff = end.getTime() - start.getTime();
            double hours = diff / (60.0 * 60.0 * 1000.0);

            double salary = 0;

            // 7-8 AM bonus
            if (times[0].equalsIgnoreCase("7:00 AM")) {
                salary += 200;
                hours -= 1; // Remove the first hour
            }

            // Base salary (8 AM - 5 PM)
            double regularHours = Math.min(9, hours);
            salary += regularHours * (1200/9);

            // Overtime (after 5 PM)
            double overtime = Math.max(hours - 9, 0);
            salary += overtime * 250;

            return salary;
        } catch (Exception e) {
            return 0;
        }
    }

    public Payment(int lNo, String name, List<WorkDay> workDays) {
        this.lNo = lNo;
        this.name = name;
        this.workDays = completeWorkDays(workDays);
        this.dayCount = (int) this.workDays.stream().filter(w -> !w.absent).count();
        calculateTotalAmount();
        this.salaryConfirmed = false;
    }

    private List<WorkDay> completeWorkDays(List<WorkDay> existingDays) {
        List<WorkDay> fullPeriod = new ArrayList<>();
        String[] allDays = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday"};

        // Get current period days
        int periodStart = 0;
        for (int i = 0; i < allDays.length; i++) {
            if (existingDays.get(0).date.equals(allDays[i])) {
                periodStart = i;
                break;
            }
        }

        for (int i = periodStart; i < periodStart + 3; i++) {
            String day = allDays[i % 6];
            boolean found = existingDays.stream().anyMatch(w -> w.date.equals(day));
            if (!found) {
                fullPeriod.add(new WorkDay(day));
            }
        }
        fullPeriod.addAll(existingDays);
        return fullPeriod;
    }

    private void calculateTotalAmount() {
        double total = workDays.stream()
                .filter(w -> !w.absent)
                .mapToDouble(w -> w.dailySalary)
                .sum();
        this.amount = "₹" + String.format(Locale.ENGLISH, "%.2f", total);
    }

    public int getLNo() {
        return lNo;
    }

    public void setlNo(int lNo) {
        this.lNo = lNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public boolean isSalaryConfirmed() {
        return salaryConfirmed;
    }

    public void setSalaryConfirmed(boolean confirmed) {
        this.salaryConfirmed = confirmed;
    }

    public List<WorkDay> getWorkDays() {
        return workDays;
    }

}
