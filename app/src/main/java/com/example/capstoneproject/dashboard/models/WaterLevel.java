package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class WaterLevel {

    @SerializedName("waterlevel_id")
    private int id;

    @SerializedName("value")
    private double value;

    @SerializedName("hydromodel_id")
    private int modelId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    // Getters
    public int getId() { return id; }
    public double getValue() { return value; }
    public int getModelId() { return modelId; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }

    // Setters (optional)
    public void setId(int id) { this.id = id; }
    public void setValue(double value) { this.value = value; }
    public void setModelId(int modelId) { this.modelId = modelId; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
}
