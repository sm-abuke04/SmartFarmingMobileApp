package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class ModelActuator {

    @SerializedName("model_actuator_id")
    private int modelActuatorId;

    @SerializedName("status")
    private String status;

    @SerializedName("control_type")
    private String controlType;

    @SerializedName("HardwareInfo")
    private HardwareInfo hardwareInfo;

    public int getModelActuatorId() { return modelActuatorId; }

    public String getStatus() { return status; }

    public HardwareInfo getHardwareInfo() { return hardwareInfo; }

    public String getActuatorName() {
        return (hardwareInfo != null) ? hardwareInfo.getActuatorName() : "Unknown Actuator";
    }
}