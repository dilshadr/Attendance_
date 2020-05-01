package com.example.attendance_app;

import android.graphics.Bitmap;

public class Holiday_List {

    private String HolidayTitle;
    private String HolidateDate;
    private Bitmap coverImage;

    public Holiday_List(){}

    public Holiday_List(String HolidayTitle, String HolidateDate,Bitmap coverImage, String HrApproval){
        this.HolidayTitle = HolidayTitle;
        this.HolidateDate = HolidateDate;
        this.coverImage = coverImage;
    }

    public String getHolidayTitle() {
        return HolidayTitle;
    }

    public void setHolidayTitle(String HolidayTitle) {

        this.HolidayTitle = HolidayTitle;
    }

    public String getHolidateDate() {
        return HolidateDate;
    }

    public void setHolidateDate(String HolidateDate) {
       this. HolidateDate = HolidateDate;
    }

    public Bitmap getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(Bitmap coverImage) {
        this.coverImage = coverImage;
    }
}
