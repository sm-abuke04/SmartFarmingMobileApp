package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ModelSensor {

    @SerializedName("model_sensor_id")
    private int modelSensorId;

    @SerializedName("hydromodel_id")
    private int hydromodelId;

    @SerializedName("sensor_id")
    private int sensorId;

    @SerializedName("status")
    private String status;

    // Use HardwareInfo class here to match your JSON structure
    @SerializedName("HardwareInfo")
    private HardwareInfo hardwareInfo;

    @SerializedName("Readings")
    private List<Reading> readings;

    public int getModelSensorId() { return modelSensorId; }
    public int getHydromodelId() { return hydromodelId; }
    public int getSensorId() { return sensorId; }
    public String getStatus() { return status; }

    // This is the method your DashboardActivity was missing
    public HardwareInfo getHardwareInfo() {
        return hardwareInfo;
    }

    public String getSensorName() {
        return (hardwareInfo != null) ? hardwareInfo.getSensorName() : "Unknown";
    }

    public List<Reading> getReadings() { return readings; }
}