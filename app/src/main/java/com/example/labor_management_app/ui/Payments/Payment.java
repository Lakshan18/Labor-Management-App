package com.example.labor_management_app.ui.Payments;

public class Payment {
    private int lNo;
    private String name;
    private String amount;
    private int dayCount;

    public Payment(int lNo, String name, String amount, int dayCount) {
        this.lNo = lNo;
        this.name = name;
        this.amount = amount;
        this.dayCount = dayCount;
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
}
