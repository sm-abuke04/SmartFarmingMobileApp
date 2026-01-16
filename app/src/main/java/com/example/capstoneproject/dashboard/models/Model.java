package com.example.capstoneproject.dashboard.models;

import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("hydromodel_id")
    private int hydromodelId;

    @SerializedName("hydromodel_name")
    private String hydromodelName;

    public int getHydromodelId() {
        return hydromodelId;
    }

    public String getHydromodelName() {
        return hydromodelName;
    }

    @Override
    public String toString() {
        return hydromodelName;
    }
}
