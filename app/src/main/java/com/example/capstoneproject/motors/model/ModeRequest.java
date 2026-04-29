package com.example.capstoneproject.motors.model;

import com.google.gson.annotations.SerializedName;

public class ModeRequest {
    @SerializedName("id") // The ID of either the Actuator or the ModelAssignment
    private int id;

    @SerializedName("control_type")
    private String controlType;

    @SerializedName("scope") // "Global" or "Model"
    private String scope;

    // Updated constructor to fix the 'cannot be applied' error
    public ModeRequest(int id, String controlType, String scope) {
        this.id = id;
        this.controlType = controlType;
        this.scope = scope;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getControlType() { return controlType; }
    public String getScope() { return scope; }
}