package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class SensorReading {

    @SerializedName("reading_value")
    private double readingValue;

    @SerializedName("record_date")
    private String recordDate;

    public double getReadingValue() {
        return readingValue;
    }

    public String getRecordDate() {
        return recordDate;
    }
}
