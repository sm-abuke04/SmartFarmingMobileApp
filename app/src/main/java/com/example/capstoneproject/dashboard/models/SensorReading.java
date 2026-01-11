package com.example.capstoneproject.dashboard.models;


import com.google.gson.annotations.SerializedName;

public class SensorReading {
    @SerializedName("temperature")
    private float temperature;

    @SerializedName("humidity")
    private float humidity;

    @SerializedName("ph")
    private float ph;

    @SerializedName("tds")
    private float tds;

    @SerializedName("light")
    private float light;

    // Getters
    public float getTemperature() { return temperature; }
    public float getHumidity() { return humidity; }
    public float getPh() { return ph; }
    public float getTds() { return tds; }
    public float getLight() { return light; }
}
