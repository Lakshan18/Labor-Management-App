package com.example.labor_management_app.ui.labor_attendance;

import java.io.Serializable;

public class Labor_Attendance_View_Model implements Serializable {

    private String name;
    private float rating;
    private int attendancePercentage;
    private String imageUrl;

    public Labor_Attendance_View_Model(String name, float rating, int attendancePercentage, String imageUrl) {
        this.name = name;
        this.rating = rating;
        this.attendancePercentage = attendancePercentage;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public float getRating() {
        return rating;
    }

    public int getAttendancePercentage() {
        return attendancePercentage;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
