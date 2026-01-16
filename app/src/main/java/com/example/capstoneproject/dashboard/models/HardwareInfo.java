package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class HardwareInfo {

    @SerializedName("sensor_name")
    private String sensor_name;

    @SerializedName("sensor_desc")
    private String sensor_desc;

    public String getSensor_name() {
        return sensor_name;
    }

    public String getSensor_desc() {
        return sensor_desc;
    }
}
